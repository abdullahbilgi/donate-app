import { useAppDispatch, useAppSelector } from "../store";
import TablesColumn from "../ui/TablesColumn";
import TablesBody from "../ui/TablesBody";
import { useParams } from "react-router";
import { TablesCell } from "../ui/TablesCell";
import Button from "../ui/Button";
import { useEffect } from "react";
import {
  acceptApply,
  getApplies,
  rejectedApply,
} from "../store/Organization/AcceptRejectApplies/thunks";
import { FaCheck } from "react-icons/fa6";
import { TiDeleteOutline } from "react-icons/ti";
import Modal from "../ui/Modal";
import { AcceptRejectedApplyModal } from "../ui/AcceptRejectedApplyModal";
import { getAllOrganizations } from "../store/Organization/thunks";

const Organization = () => {
  const dispatch = useAppDispatch();
  const { id } = useParams();
  const { Organizations, loading } = useAppSelector(
    (state) => state.Organization
  );

  console.log(Organizations);
  useEffect(() => {
    dispatch(getAllOrganizations());
  }, []);

  return (
    <div className="group-data-[sidebar-size=lg]:ltr:md:ml-vertical-menu group-data-[sidebar-size=lg]:rtl:md:mr-vertical-menu p-8 group-data-[sidebar-size=md]:ltr:ml-vertical-menu-md group-data-[sidebar-size=md]:rtl:mr-vertical-menu-md group-data-[sidebar-size=sm]:ltr:ml-vertical-menu-sm group-data-[sidebar-size=sm]:rtl:mr-vertical-menu-sm pt-[calc(theme('spacing.header')_*_1)] pb-[calc(theme('spacing.header')_*_0.8)] px-4 group-data-[navbar=bordered]:pt-[calc(theme('spacing.header')_*_1.3)] group-data-[navbar=hidden]:pt-0 group-data-[layout=horizontal]:mx-auto group-data-[layout=horizontal]:max-w-screen-2xl group-data-[layout=horizontal]:px-0 group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:ltr:md:ml-auto group-data-[layout=horizontal]:group-data-[sidebar-size=lg]:rtl:md:mr-auto group-data-[layout=horizontal]:md:pt-[calc(theme('spacing.header')_*_1.8)] group-data-[layout=horizontal]:px-3 group-data-[layout=horizontal]:group-data-[navbar=hidden]:pt-[calc(theme('spacing.header')_*_0.9)]">
      {!loading && Organizations.length === 0 && (
        <div className="flex justify-between items-center p-4 mt-5 bg-yellow-100 mb-4">
          <h3 className="font-medium text-md">
            There are no organization at this time!
          </h3>
        </div>
      )}
      <div className="container-fluid group-data-[content=boxed]:max-w-boxed mx-auto bg-gray-50 p-6 shadow-md shadow-slate-200">
        <div className="my-2 col-span-12 overflow-x-auto lg:col-span-12">
          <h6 className="mb-4 text-2xl">All Organizations</h6>
          <table className="bordered group dataTable w-full text-sm align-middle whitespace-nowrap no-footer">
            <thead className="border-b border-slate-200 dark:border-zink-500">
              <tr className="group-[.stripe]:even:bg-slate-50 group-[.stripe]:dark:even:bg-zink-600 transition-all duration-150 ease-linear group-[.hover]:hover:bg-slate-50 dark:group-[.hover]:hover:bg-zink-600 [&.selected]:bg-custom-500 dark:[&.selected]:bg-custom-500 [&.selected]:text-custom-50 dark:[&.selected]:text-custom-50">
                <TablesColumn title="Name" />
                <TablesColumn title="City" />
                <TablesColumn title="Full Address" />
                <TablesColumn title="Tax Number" />
                <TablesColumn title="Owner's Name Surname" />
                <TablesColumn title="Phone" />
                <TablesColumn title="Email" />
                <TablesColumn title="Status" />
                <TablesColumn title="" />
              </tr>
            </thead>
            <tbody>
              {Organizations.map((apply) => {
                return (
                  <TablesBody>
                    <TablesCell>{apply.name}</TablesCell>
                    <TablesCell>
                      {apply.address.region.name},{" "}
                      {apply.address.region.city.name}
                    </TablesCell>
                    <TablesCell>{apply.address.name}</TablesCell>
                    <TablesCell>{apply.taxNumber}</TablesCell>
                    <TablesCell>
                      {apply.user.name} {apply.user.surname}
                    </TablesCell>
                    <TablesCell>{apply.user.phone}</TablesCell>
                    <TablesCell>{apply.user.email}</TablesCell>

                    <TablesCell>
                      <p
                        className={`${
                          apply.status === "PENDING"
                            ? "text-yellow-500"
                            : "text-green-600"
                        } font-semibold`}
                      >
                        {apply.status}
                      </p>
                    </TablesCell>
                  </TablesBody>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Organization;
